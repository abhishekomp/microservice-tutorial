document.addEventListener('alpine:init', () => {
    Alpine.data('initData', (pageNo) => ({
            count: 0,
            init() {
                this.count = 25;
            }
        }))
});