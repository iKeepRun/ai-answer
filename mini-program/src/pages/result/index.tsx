import { View, Image } from '@tarojs/components'

import { AtButton } from 'taro-ui'
import Taro from '@tarojs/taro'
import './index.scss'
import GlobalFooter from '../../components/GlobalFooter'
import questionResult from "../../data/question_results.json"
import questions from "../../data/questions.json"
import {findPersonalityType} from "../../utils/findResult"
import homebg from '../../assert/homebg.jpg'
//引入findResult函数
// import findResult 


export default () => {
  // const result = questionResult[0]
  const answerList= Taro.getStorageSync('answerList')
  
  

  const result=findPersonalityType(answerList,questions, questionResult)

  console.log("输出结果：",result)
  return (
    <View className='resultPage'>
      <view className='at-article__h1 title'>{result.resultName}</view>
      <view className='at-article__h3 subTitle'>
        {result.resultDesc}
      </view>
      <AtButton type='primary'
        size='normal'
        className='enterBtn'
        circle
        onClick={() => {
          Taro.reLaunch({
            url: "/pages/index/index"
          })
        }}
      >
        返回主页
      </AtButton>
      <Image src={homebg} style={{ width: "100%" }} mode='aspectFill' />
      <GlobalFooter />
    </View>
  )
}
