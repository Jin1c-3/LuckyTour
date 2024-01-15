import axios from "axios";

const service = axios.create({
  baseURL: import.meta.env.VITE_SEVER_API_URL,
  timeout: 5000,
});

service.interceptors.request.use(
  (config) => {
    if (localStorage.getItem("token")) {
      config.headers["X-token"] = localStorage.getItem("token");
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

service.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    return Promise.reject(error);
  }
);

const service_ = axios.create({
  baseURL: import.meta.env.VITE_COMPUTE_API_URL,
  timeout: 10000,
});

export { service, service_ };
