import Vue from 'vue'
import { 
    Swipe, 
    SwipeItem,
    TabContainer,
    TabContainerItem,
    InfiniteScroll,
    Actionsheet,
    Popup,
    Lazyload,
    Cell
} from 'mint-ui'



Vue.component(Swipe.name, Swipe);
Vue.component(SwipeItem.name, SwipeItem);
Vue.component(TabContainer.name, TabContainer);
Vue.component(TabContainerItem.name, TabContainerItem);
Vue.component(Actionsheet.name, Actionsheet);
Vue.component(Popup.name, Popup);
Vue.use(InfiniteScroll);
Vue.use(Lazyload);
Vue.component(Cell.name, Cell);