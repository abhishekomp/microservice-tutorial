document.addEventListener('alpine:init', () => {
    Alpine.data('initData', (pageNo) => ({
            count: 0,
            pageNo: pageNo,
            init() {
                this.count = 25;
                console.log("Page No:", this.pageNo)
            }
        }))
});