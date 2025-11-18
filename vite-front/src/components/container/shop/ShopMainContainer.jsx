import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router';

const ShopMainContainer = () => {

  const [items, setItems] = useState([]);

  const fetchData = async ()=>{
    const response = await axios.get("http://localhost:8088/api/shop");

    if(response.data && response.data.content){
      setItems(response.data.content);
    }
  };

  useEffect(()=>{
    fetchData();
  },[]);

  return (
    <div className="itemList">
      
      <h3>this section for HEADER ::</h3>
      <h3>this section for HEADER ::</h3>

      <div className="itemList-con">
        <h2> :: 상품목록 :: </h2>
        <table className='item-table'>
          <thead>
            <tr>
              <th scope='col'>:: ID</th>
              <th scope='col'>:: 상품명</th>
              <th scope='col'>:: 상품가격</th>
            </tr>
          </thead>
          <tbody>
            {console.log(items)}
            { items.map(list=>(
              <tr key={list.id}>
                <td>{list.id}</td>
                <td><Link to={`/store/detail/${list.id}`} className='board-link'>{list.itemTitle}</Link></td>
                <td>{list.itemPrice}</td>
              </tr>
            ))}
            </tbody>
        </table>

      </div>
    </div>
  )
}

export default ShopMainContainer