(ns day1
  (:require [clojure.edn :as edn]))

(def example-input [199
                    200
                    208
                    210
                    200
                    207
                    240
                    269
                    260
                    263])

(defn count-increases [depths]
  (->> depths
       (partition 2 1)
       (filter (fn [[a b]] (> b a)))
       count))

(comment
  (count-increases example-input)

  (->> (slurp "day1input.txt")
       (format "[%s]")
       edn/read-string
       count-increases))

;; part2

(comment
  (->> example-input
       (partition 3 1)
       (map #(apply + %))
       count-increases)

  (->> (slurp "day1input.txt")
       (format "[%s]")
       edn/read-string
       (partition 3 1)
       (map #(apply + %))
       count-increases))