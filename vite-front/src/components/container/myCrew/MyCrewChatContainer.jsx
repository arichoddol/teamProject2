import { Client } from '@stomp/stompjs'
import React, { useEffect, useRef, useState } from 'react'
import { useSelector } from 'react-redux'
import { useParams } from 'react-router'
import SockJS from 'sockjs-client'
import jwtAxios from '../../../apis/util/jwtUtil'

const MyCrewChatContainer = () => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken)
  const senderId = useSelector((state) => state.loginSlice.id)
  const senderNickName = useSelector((state) => state.loginSlice.nickName)
  const {crewId} = useParams()

  const [messages, setMessages] = useState([])
  const [input, setInput] = useState("")
  const stompRef = useRef(null)
  const messagesEndRef = useRef(null)

  useEffect(() => {
    // 최근 대화 300개 불러오기
    const fetchRecentMessages = async () => {
      try {
        const res = await jwtAxios.get(`/api/mycrew/${crewId}/chat/recent?limit=300`,
          {
            headers: { Authorization: `Bearer ${accessToken}`},
            withCredentials: true
          }
        )
        setMessages(res.data.reverse())
      } catch(err) {
          console.log("대화 로드 실패", err)
      }
    }
    fetchRecentMessages()
  }, [crewId, accessToken])

  useEffect(() => {
    // stomp 연결
    const socket = new SockJS("http://localhost:8088/ws")
    const stomp = new Client({
      webSocketFactory: () => socket,
      debug: (str) => {console.log(str)},
      onConnect: () => {
        console.log("연결됨")
        // 크루끼리
        stomp.subscribe(`/topic/chat/crew/${crewId}`, (payload) => {
          const msg = JSON.parse(payload.body)
          setMessages(prev => {
            // 중복 방지
            if (prev.some(m => m.id && m.id === msg.id)) return prev
            return [...prev, msg]
          })
        })
      },
      onStompError: (err) => {
        console.error("stomp 에러", err)
      }
    })

    stompRef.current = stomp
    stomp.activate()

    return () => {
      if (stompRef.current) {
        stompRef.current.deactivate()
        stompRef.current = null;
      }
    }
  }, [crewId])

  const sendMessage = () => {
    if (!input.trim()) return;
    if (!stompRef.current?.connected) {
      console.log("연결 대기")
      return
    }
    const payload = {
      crewId,
      senderId,
      senderNickName,
      message: input.trim()
    }
    // 전송
    stompRef.current?.publish({
      destination: `/app/chat/crew/${crewId}`,
      body: JSON.stringify(payload)
    })
    setInput("")
  }

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({behavior: "smooth"})
  }, [messages])

  return (
    <div className="crewChat">
      <div className="crewChat-con">
        <div className="recentMessages">
          {messages.map((m) => (
            <div className={`crewMessage ${m.senderId === senderId ? "me" : "other"}`} 
                 key={m.id}>
              <strong>{m.senderNickName}</strong>
              <div className='amessage'>{m.message}</div>
              <span className='time'>{new Date(m.createTime).toLocaleString()}</span>
            </div>
          ))}
          <div ref={messagesEndRef}></div>
        </div>
        <div className='writeMessage'>
          <input
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => { if (e.key === "Enter") sendMessage(); }}
          />
          <button onClick={sendMessage}>전송</button>
        </div>
      </div>
    </div>
  )
}

export default MyCrewChatContainer