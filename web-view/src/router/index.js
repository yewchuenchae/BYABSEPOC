import Vue from 'vue'
import Router from 'vue-router'
import publicRouter from './public-router'
import { getRouters} from './routerFun'
Vue.use(Router)

const router = new Router({
  routes: []
})
// 动态加载路由条件参数 true已加载 false未加载
let on_off = false;
// 刷新页面判断参数 true初次加载 false非初次加载
let first = true;
router.beforeEach(async (to, from, next)=> {
  // 加载动态路由
  if(!on_off){
    await router.addRoutes(publicRouter);
    await getRouters(router,()=>{
      on_off = true;
      next();
    })
  }
  // 判断跳转路由是否存在
  if(to.matched.length == 0){
    from.name ? next(from) : next({name: 'home'})
    return
  }
  // 初次加载且不为跟路由
  if(first){
    first = false;
    next(from)
  }else{
    next();
  }
})

export default router;