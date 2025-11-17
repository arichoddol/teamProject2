
import './App.css'
import { RouterProvider, useNavigate } from 'react-router-dom'
import root from './router/root';

function App() {

  return (
    <>
      {/* <ScrollTopBtn/> */}
      <RouterProvider router={root}/>
    </>
  )
}

export default App;
