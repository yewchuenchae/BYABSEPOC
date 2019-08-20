import Vue from 'vue'
import { 
    Swipe, 
    SwipeItem,
    TabContainer,
    TabContainerItem,
    InfiniteScroll,
} from 'mint-ui'


Vue.component(Swipe.name, Swipe);
Vue.component(SwipeItem.name, SwipeItem);
Vue.component(TabContainer.name, TabContainer);
Vue.component(TabContainerItem.name, TabContainerItem);
Vue.use(InfiniteScroll);