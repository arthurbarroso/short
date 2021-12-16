(ns short.middlewares-test
  (:require [short.middlewares :as mi]
            [clojure.test :refer [deftest testing is]]
            [short.users.handlers :refer [gen-token!]]))

(deftest middlewares-exception-handler-test
  (testing "Returns an exception map"
    (let [res-map (mi/exception-handler
                   "Something wrong happened"
                   (Exception. "Some expcetion")
                   {:uri "test"})]
      (is (= (:status res-map) 500))
      (is (= "Something wrong happened"
             (-> res-map :body :message))))))

(deftest middlewares-wrap-jwt-auth-test
  (testing "Attaches the `identity` property/keyword to an authenticated
            request map"
    (let [env {:auth {:jwt-secret "hi"}}
          mid (mi/wrap-jwt-auth identity env)
          token (gen-token!
                 {:matches? true :existing-user {:user/email "arthur@email.com"}}
                 env)
          result (mid {:headers {:Authorization (str "Token " (:token token))}
                       :body {:data "oi"}})]
      (is (= (:identity result) {:user/email "arthur@email.com"})))))

(deftest middlewares-auth-middleware-test
  (testing "Leaves an authenticated request untouched"
    (let [env {:auth {:jwt-secret "hi"}}
          mid (mi/wrap-jwt-auth identity env)
          mid-final (mi/auth-middleware identity)
          token (gen-token! {:matches? true :existing-user {:user/email "arthur@email.com"}} env)
          request {:headers {:Authorization (str "Token " (:token token))
                             :body {:hi "there"}}}
          result (mid-final (mid request))]
      (is (= (:identity result) {:user/email "arthur@email.com"}))
      (is (= result (assoc request :identity {:user/email "arthur@email.com"})))))
  (testing "Rejects unauthenticated requests"
    (let [env {:auth {:jwt-secret "hi"}}
          mid (mi/wrap-jwt-auth identity env)
          mid-final (mi/auth-middleware identity)
          request {:body {:hi "there"}}
          {:keys [status body]} (mid-final (mid request))]
      (is (= body {:error "Unauthorized"}))
      (is (= 401 status)))))
