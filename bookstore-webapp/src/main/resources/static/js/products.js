document.addEventListener('alpine:init', () => {
    Alpine.data('initData', (pageNo) => ({
            count: 0,
            pageNo: pageNo,
            products:   {
                data: []
            },
            init() {
                this.count = 25;
                console.log("Page No:", this.pageNo)
                this.loadProducts(this.pageNo);
            },
            loadProducts(pageNo) {
               $.getJSON("http://localhost:8989/catalog/api/products?page="+pageNo, (resp)=> {
                      console.log("Products Resp:", resp)
                      this.products = resp;
               });
            }
        }))
});