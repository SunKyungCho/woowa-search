(() => {
    const utils = {
        getUrlParams(search) {
            let hashes = search.slice(search.indexOf('?') + 1).split('&')
            return hashes.reduce((params, hash) => {
                let [key, val] = hash.split('=')
                return Object.assign(params, {[key]: decodeURIComponent(val)})
            }, {})
        }
    }

    const searchParams = {}

    const api = {
        getShops(params) {
            return axios.get('/shops', {
                headers: {
                    'Content-Type': 'application/json'
                },
                params
            })
        },
        getShop(shopNumber) {
            return axios.get(`/shops/${shopNumber}`, { headers: {'Content-Type': 'application/json'} })
        },
        writeShop(data) {
            return axios.post('/shops', data, { headers: {'Content-Type': 'application/json'} })
        },
        updateShop(shopNumber, data) {
            return axios.put(`/shops/${shopNumber}`, data, { headers: {'Content-Type': 'application/json'} })
        },
        deleteShop(shopNumber) {
            return axios.delete(`/shops/${shopNumber}`, { headers: {'Content-Type': 'application/json'} })
        }
    }

    const activeBtn = () => {
        $('#openFilter').removeClass('activeBtn');
        $('#scoreSort').removeClass('activeBtn');
        $('#keyword').val(searchParams.shopName);

        if(searchParams.filter) $('#openFilter').addClass('activeBtn');
        if(searchParams.sort) $('#scoreSort').addClass('activeBtn');
    };

    const baseView = {
        async _load() {
            await this._init()
            await this._render()
            this._loadComplete = true
        },
        async _init() {
            this.$el = $(this.el)
            await this.init()
        },
        async _render() {
            this.$el.html(Handlebars.compile(await this.render())(this))
            activeBtn();
        }
    }

    const views = {
        main :(() => {
            return {
                el: '#container',
                async getShops() {
                    try {
                        let resp = await api.getShops()
                        this.shops = resp.data
                    } catch (e) {
                        alert('가게 목록 호출에 실패하였습니다.')
                    }
                },
                async openFilter() {
                    try {
                        if (searchParams.filter) {
                            delete searchParams.filter;
                        } else {
                            searchParams.filter = 'OPEN';
                        }

                        let resp = await api.getShops(searchParams);
                        this.shops = resp.data
                    } catch (e) {
                        alert('가게 목록 호출에 실패하였습니다.')
                    }
                },
                async scoreSort() {
                    try {
                        if (searchParams.sort) {
                            delete searchParams.sort;
                        } else {
                            searchParams.sort = 'SCORE';
                        }

                        let resp = await api.getShops(searchParams);
                        this.shops = resp.data
                    } catch (e) {
                        alert('가게 목록 호출에 실패하였습니다.')
                    }
                },
                async searchShops(e) {
                    try {
                        if (e.keyCode === 13 || e.target === 'button#goSearch') {
                            const keyword = $('#keyword').val();
                            if (!keyword) {
                                delete searchParams.shopName;
                            } else {
                                searchParams.shopName = keyword;
                            }

                            let resp = await api.getShops(searchParams);
                            this.shops = resp.data
                        }
                    } catch (e) {
                        alert('가게 목록 호출에 실패하였습니다.')
                    }
                },
                async deleteShop(e) {
                    try {
                        let shopNumber = $($(e.target).closest('li')).data('id')
                        await api.deleteShop(shopNumber)
                        alert("삭제 되었습니다.");
                        await this.getShops()
                    } catch (e) {
                        alert('가게 삭제에 실패하였습니다.')
                    }
                },
                goDetail(e) {
                    let shopNumber = $($(e.target).closest('li')).data('id')
                    window.location.hash = 'detail' + (shopNumber ? ('?shopNumber=' + shopNumber) : '')
                },
                init() {
                    this.getShops()
                    this.$el.on('click', '#goInsert', this.goDetail.bind(this))
                    this.$el.on('click', '.detail_btn', this.goDetail.bind(this))
                    this.$el.on('click', '.delete_btn', this.deleteShop.bind(this))
                    this.$el.on('click', '#openFilter', this.openFilter.bind(this))
                    this.$el.on('click', '#scoreSort', this.scoreSort.bind(this))
                    this.$el.on('click', '#goSearch', this.searchShops.bind(this))
                    this.$el.on('keydown', '#keyword', this.searchShops.bind(this))
                },
                render() {
                    return `
                       <div class="entry">
                            <h1>가게 목록</h1>
                            <div id="searchBox">
                                <div>
                                    <input id="keyword">
                                    <button id="goSearch">검색</button>
                                </div>
                                <button id="openFilter">오픈가게만</button>
                                <button id="scoreSort">랭킹높은순</button>
                            </div>
                            <div>
                                <button id="goInsert">추가</button>
                            </div>
                            <div class="body">
                              {{#each shops}}
                              <ul>
                                <li>{{shopNumber}}</li>
                                <li>{{shopName}}</li>
                                <li>{{address}}</li>
                                <li>맛집 점수 {{score}}</li>
                                <li>운영여부 {{isOpen}}</li>
                                <li data-id="{{shopNumber}}">
                                    <button class="detail_btn">수정</button>
                                    <button class="delete_btn">삭제</button>
                                </li>
                              </ul>
                              {{/each}}
                            </div>
                        </div>`
                }
            }
        })(),
        detail : (() => {
            return {
                el: '#container',
                async getShop() {
                    try {
                        let resp = await api.getShop(this.params.shopNumber);
                        this.shop = resp.data
                    } catch (e) {
                        console.error(e)
                        alert('가게 호출에 실패하였습니다.')
                    }
                },
                async insertShop() {
                    try {
                        await api.writeShop({
                            shopName: this.$el.find('#shopName')[0].value,
                            shopNumber: this.$el.find('#shopNumber')[0].value,
                            address: this.$el.find('#address')[0].value,
                            lat: this.$el.find('#lat')[0].value,
                            lon: this.$el.find('#lon')[0].value,
                            isOpen: this.$el.find('#isOpen')[0].value,
                            score: this.$el.find('#score')[0].value
                        })
                        this.goHome()
                    } catch (e) {
                        alert('가게 추가에 실패하였습니다.')
                    }
                },
                async updateShop() {
                    try {
                        await api.updateShop(this.params.shopNumber, {
                            shopName: this.$el.find('#shopName')[0].value,
                            shopNumber: this.$el.find('#shopNumber')[0].value,
                            address: this.$el.find('#address')[0].value,
                            lat: this.$el.find('#lat')[0].value,
                            lon: this.$el.find('#lon')[0].value,
                            isOpen: this.$el.find('#isOpen')[0].value,
                            score: this.$el.find('#score')[0].value
                        })
                        this.goHome()
                    } catch (e) {
                        console.error(e)
                        alert('가게 수정에 실패하였습니다.')
                    }
                },
                goHome() {
                    window.location.hash = ''
                },
                init() {
                    this.$el.on('click', '#back', this.goHome.bind(this))
                    this.$el.on('click', '#update', this.updateShop.bind(this))
                    this.$el.on('click', '#insert', this.insertShop.bind(this))
                    this.params = utils.getUrlParams(window.location.hash)
                    if(this.params.shopNumber) {
                        this.getShop(this.params.shopNumber)
                    }
                },
                render() {
                    return `
                    <div class="entry">
                        <h1>가게 상세</h1>
                        <div>
                            <button id="back">돌아가기</button>
                            {{#if params.shopNumber}}
                            <button id="update">수정</button>
                            {{else}}
                            <button id="insert">추가</button>
                            {{/if}}
                        </div>
                        <div class="body">
                            <ul>가게 번호 : <input type="text" id="shopNumber" value="{{shop.shopNumber}}"></ul>
                            <ul>가게 이름 : <input type="text" id="shopName" value="{{shop.shopName}}"></ul>
                            <ul>주소 : <input type="text" id="address" value="{{shop.address}}"></ul>
                            <ul>경도 : <input type="text" id="lat" value="{{shop.lat}}"></ul>
                            <ul>위도 : <input type="text" id="lon" value="{{shop.lon}}"></ul>
                            <ul>운영 여부
                                <select id="isOpen" name="isOpen">
                                    <option value="true" selected="{{shop.isOpen}} == true ? selected : ''">운영</option>
                                    <option value="false" selected="{{shop.isOpen}} == false ? selected : ''">비운영</option>
                                </select>
                            </ul>
                            <ul>맛집 점수 : <input type="text" id="score" value="{{shop.score}}"></ul>
                        </div>
                    </div>
                    `
                }
            }
        })()
    }

    const router = (hash) => {
        if(hash === '' || hash === '#') {
            return views.main
        } else if (hash.includes('#detail')) {
            return views.detail
        }
    }

    const proxy = (route) => {
        let assign = Object.assign({}, route, baseView)
        return new Proxy(assign, {
            set(target, key, value) {
                target[key] = value
                if(assign._loadComplete) {
                    assign._render()
                }
            }
        })
    }

    const start = () => {
        if(this._view) {
            this._view.$el.off()
        }
        this._view = proxy(router(window.location.hash))
        this._view._load()
    }
    window.addEventListener("hashchange", start, false)
    start()
})()