import ACCESS_ENUM from '@/access/accessEnum'

const checkAccess = (needAccess = ACCESS_ENUM.GUEST, loginUser: API.LoginUserVO) => {
  const loginUserAccess = loginUser?.userRole ?? ACCESS_ENUM.GUEST
  if (needAccess === ACCESS_ENUM.GUEST) {
    return true
  }

  if (needAccess === ACCESS_ENUM.ADMIN) {
    if (loginUserAccess !== ACCESS_ENUM.ADMIN) {
      return false
    }
  }
  if (needAccess === ACCESS_ENUM.USER) {
    if (loginUserAccess === ACCESS_ENUM.GUEST) {
      return false
    }
  }

  return true
}

export default checkAccess
