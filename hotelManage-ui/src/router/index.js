import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录', noLayout: true }
  },
  {
    path: '/',
    component: () => import('@/layout/MainLayout.vue'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'room-type',
        name: 'RoomType',
        component: () => import('@/views/room/RoomTypeList.vue'),
        meta: { title: '房型管理' }
      },
      {
        path: 'room',
        name: 'Room',
        component: () => import('@/views/room/RoomList.vue'),
        meta: { title: '房间管理' }
      },
      {
        path: 'reservation',
        name: 'Reservation',
        component: () => import('@/views/reservation/ReservationList.vue'),
        meta: { title: '预订管理' }
      },
      {
        path: 'check-in',
        name: 'CheckIn',
        component: () => import('@/views/checkin/CheckInList.vue'),
        meta: { title: '入住管理' }
      },
      {
        path: 'check-out',
        name: 'CheckOut',
        component: () => import('@/views/checkin/CheckOutList.vue'),
        meta: { title: '退房结账' }
      },
      {
        path: 'guest',
        name: 'Guest',
        component: () => import('@/views/user/GuestList.vue'),
        meta: { title: '普通客人' }
      },
      {
        path: 'member',
        name: 'Member',
        component: () => import('@/views/user/MemberList.vue'),
        meta: { title: '会员档案' }
      },
      {
        path: 'member-level',
        name: 'MemberLevel',
        component: () => import('@/views/user/MemberLevelList.vue'),
        meta: { title: '会员等级' }
      },
      {
        path: 'member-statistics',
        name: 'MemberStatistics',
        component: () => import('@/views/user/MemberStatistics.vue'),
        meta: { title: '会员统计' }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('@/views/system/RoleList.vue'),
        meta: { title: '角色管理' }
      },
      {
        path: 'system-user',
        name: 'SystemUser',
        component: () => import('@/views/system/SystemUserList.vue'),
        meta: { title: '系统用户' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

const whiteList = ['/login']

router.beforeEach((to, _from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 酒店客房管理系统` : '酒店客房管理系统'
  const token = localStorage.getItem('token')
  if (token) {
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      next()
    }
  } else {
    if (whiteList.includes(to.path)) {
      next()
    } else {
      next(`/login?redirect=${encodeURIComponent(to.fullPath)}`)
    }
  }
})

export default router
