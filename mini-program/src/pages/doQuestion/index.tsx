import { View } from '@tarojs/components'
import { AtButton,  AtRadio } from 'taro-ui'

import { useEffect, useState } from 'react'
import Taro from '@tarojs/taro'

import './index.scss'
import GlobalFooter from '../../components/GlobalFooter'
import questions from "../../data/questions.json"


export default () => {

  const [current, setCurrent] = useState<number>(1)
  const [currentQuestion, setCurrentQuestion] = useState(questions[0])

  const [currentAnswer, setCurrentAnswer] = useState<string>()

  const [answerList]=useState<string[]>([])
  
  const radioOptions = currentQuestion.options.map(item => {
    return {
      label: `${item.key}.${item.value}`,
      value: item.key
    }
  })

  useEffect(()=>{
    setCurrentQuestion(questions[current-1])
    setCurrentAnswer(answerList[current-1])

  },[current])
  
  function handlerResult(){
     Taro.navigateTo({
      url:"/pages/result/index"
     })
    //存储答案列表到本地存储
    Taro.setStorageSync('answerList',answerList)
  }


  return (
    <View className='doQuestionPage'>
      <view className='at-article__h1 title'>{current}、{currentQuestion.title}</view>
      <AtRadio
        options={radioOptions}
        value={currentAnswer}
       
        onClick={(value) => { 
          setCurrentAnswer(value)
          answerList[current-1]=value
        }}
      />

      {current < questions.length && (
        <AtButton type='primary'
          size='normal'
          className='selectBtn'
          circle
          disabled={!currentAnswer}
          onClick={
            () => { 
              setCurrent(current + 1) 
            }
          }
        >
          下一题
        </AtButton>) 
      }
     
      {
      current==questions.length&&currentAnswer!=null&&(<AtButton type='primary'
        size='normal'
        className='selectBtn'
        circle
        onClick={handlerResult}
      >
        查看结果
      </AtButton>)
      }
      
      {current>1&&( <AtButton 
        size='normal'
        className='selectBtn'
        circle
        onClick={()=>{setCurrent(current-1)}}
      >
        上一题
      </AtButton>)}
     
      <GlobalFooter />
    </View>
  )
}
