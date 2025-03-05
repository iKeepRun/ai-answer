import { View, Image } from '@tarojs/components'
import { AtButton } from 'taro-ui'
import Taro from '@tarojs/taro'
import homebg from "../../assert/homebg.jpg"

import './index.scss'
import GlobalFooter from '../../components/GlobalFooter'

export default ()=>{
    return (
      <View className='indexPage'>
        <view className='at-article__h1 title'>MBTI 性格测试</view>
        <view className='at-article__h3 subTitle'>
          只需要2分钟，就能非常准确地描述出你是谁,以及你的性格特点
        </view>
        <AtButton type='primary'
          size='normal'
          className='enterBtn'
          circle
          onClick={() => {
            Taro.navigateTo({
              url: "/pages/doQuestion/index"
            })  
          }}
        >
          开始测试
        </AtButton>
        <Image src={homebg}  style={{width:"100%" }} mode='aspectFill' />
        <GlobalFooter />
      </View>
    )
}
