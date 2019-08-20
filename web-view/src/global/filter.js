import Vue from "vue";

const filters = {
    myFilter(value){
        if (value){
            return value+'111'
        }
    }
}

for(let i in filters){
    Vue.filter(i,filters[i])
}