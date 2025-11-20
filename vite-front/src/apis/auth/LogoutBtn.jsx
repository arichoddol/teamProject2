import store from "../../store/store";
import axios from "axios";
import { BACK_BASIC_URL } from "../commonApis";
import { useDispatch } from "react-redux";
import { logoutAction } from "../../slices/loginSlice";
import { useNavigate } from "react-router";

const LogoutBtn = () => {
  const ACCESS_TOKEN_KEY = "accessToken";
  const accessToken = store.getState().jwtSlice.accessToken;
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const logoutFn = async () => {
    try {
      const res = await axios.post(
        `${BACK_BASIC_URL}/api/member/logout`,
        {},
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
          withCredentials: true,
        }
      );

      localStorage.removeItem(ACCESS_TOKEN_KEY);
      dispatch(logoutAction());
      alert("로그아웃 처리되었습니다!");
      navigate("/store/index");

      return res.status;
    } catch (err) {
      localStorage.removeItem(ACCESS_TOKEN_KEY);
      dispatch(logoutAction());
      alert("로그아웃 처리되었습니다!");
      navigate("/store/index");
      console.log("로그아웃 처리 중 오류 발생:", err);
    }
  };

  return (
    <li onClick={logoutFn} className="logout-button">
      LOGOUT
    </li>
  );
};

export default LogoutBtn;
