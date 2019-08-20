import privateRouter from './private-router'

export async function getRouters(router, cb){
    await setTimeout(() => {
        router.addRoutes(privateRouter)
    }, 0);
    cb && cb()
}