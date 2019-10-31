// const Home = () => import('@/pages/home')   // 首页
const List = () => import('@/pages/list')   // 列表页
const Details = () => import('@/pages/details')   // 详情页
// const Test = () => import('@/pages/test')   //  测试页

const PCHOME = ()=> import('@/pages/pcPages/home')  //pc页面
const Company = ()=> import('@/pages/company')  //h5

export default [
    {
        path: '/',
        redirect: to => {
            return '/company'
        }
    },
    // {
    //     path: '/home',
    //     name: 'home',
    //     components: {
    //         default: Home,
    //     },
    // },
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
    {
        path: '/pcHome',
        name: 'pcHome',
        components: {
            default: PCHOME
        }
    },
    {
        path: '/company',
        name: 'company',
        components: {
            default: Company
        }
    }
    // {
    //     path: '/test',
    //     name: 'test',
    //     components: {
    //         default: Test
    //     }
    // }
]