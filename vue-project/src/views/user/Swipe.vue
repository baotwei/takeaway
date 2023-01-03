<template>
    <van-swipe :autoplay="3000">
        <van-swipe-item v-for="(image, index) in images" :key="index">
            <img v-lazy="image" style="max-width: 100%;width: 80%;height: 150px" />
        </van-swipe-item>
    </van-swipe>
</template>

<script>
    export default {
        name: "Swipe",
        data() {
            return {
                images: [
                    /*'/upload/p1.png',
                    '/upload/p2.png',
                    '/upload/p3.png',
                    '/upload/p4.png',*/
                    '/upload/p5.png',
                    '/upload/p6.png',
                    '/upload/p7.png',

                ],
            };
        }
    }
</script>

<style scoped>

</style>
