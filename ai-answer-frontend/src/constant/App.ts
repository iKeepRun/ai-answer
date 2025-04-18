export const APP_TYPE_ENUM = {
  SCORE: 0,
  TEST: 1,
}

export interface AppTypeMap {
  [key: number]: string
}

export const APP_TYPE_MAP: AppTypeMap = {
  0: '得分类',
  1: '测评类',
}

export const REVIEW_STATUS_ENUM = {
  REVIEWING: 0,
  PASS: 1,
  REJECT: 2,
}

export interface ReviewStatusMap {
  [key: number]: string
}

export const REVIEW_STATUS_MAP = {
  0: '待审核',
  1: '通过',
  2: '拒绝',
}

export const SCORING_STRATEGY_ENUM = {
  CUSTOM: 0,
  AI: 1,
}
export interface ScoringStrategyMap {
  [key: number]: string
}
export const SCORING_STRATEGY_MAP: ScoringStrategyMap = {
  0: '自定义',
  1: 'AI',
}
