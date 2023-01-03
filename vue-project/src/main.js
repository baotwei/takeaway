import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import {Lazyload} from 'vant';

import { Toast } from 'vant';
Vue.use(Toast);


/*引入axios*/
import axios from "axios";
import VueAxios from "vue-axios";
Vue.use(VueAxios,axios);
//Vue.prototype.$axios = axios;

Vue.use(Lazyload);

/*引入Vant*/
import Vant from 'vant';
import 'vant/lib/index.css';
Vue.use(Vant);

/*引入element-ui*/
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI);

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
