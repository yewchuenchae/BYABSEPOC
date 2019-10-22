const Home = () => import('@/pages/home')   // 首页
const List = () => import('@/pages/list')   // 列表页
const Details = () => import('@/pages/details')   // 详情页
// const Test = () => import('@/pages/test')   //  测试页

export default [
    {
        path: '/',
        redirect: to => {
            return '/home'
        }
    },
    {
        path: '/home',
        name: 'home',
        components: {
            default: Home,
        },
    },
    {
        path: '/list',
        name: 'list',
        components: {
            default: List,
        },
    }, 
    {
        path: '/details',
        name: 'details',
        components: {
            default: Details
        }
    },
    // {
    //     path: '/test',
    //     name: 'test',
    //     components: {
    //         default: Test
    //     }
    // }
]