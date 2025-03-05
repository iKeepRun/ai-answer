import { Component, PropsWithChildren } from 'react'
import 'taro-ui/dist/style/index.scss'
import Taro, {useLaunch} from '@tarojs/taro'
import './app.scss'

// class App extends Component<PropsWithChildren>  {

//   componentDidMount () {}

//   componentDidShow () {}

//   componentDidHide () {}

//   useLaunch(async ()=>{
//    const code= await Taro.login()
//    console.log(code)
//   })

//   // this.props.children 是将要会渲染的页面
//   // 定义一个名为 render 的方法，该方法用于渲染组件的内容
//   render () {
//     // 返回 this.props.children，即组件的子元素
//     // 这意味着组件将渲染其父组件传递给它的任何子元素
//     return this.props.children
//   }
// }


function App({children}: PropsWithChildren) {
  useLaunch(async ()=>{
    const code= await Taro.login()
    console.log("授权码",code)
  })
  return children;

}

export default App
