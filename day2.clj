(ns day2
  (:require [clojure.edn :as edn]))

(def example-input '[[forward 5]
                     [down 5]
                     [forward 8]
                     [up 3]
                     [down 8]
                     [forward 2]])

(defn answer-1 [instructions]
  (let [[horiz depth]
        (reduce (fn [[horiz depth] [direction amount]]
                  (case direction
                    forward [(+ horiz amount) depth]
                    down    [horiz (+ depth amount)]
                    up      [horiz (- depth amount)]))
                [0 0]
                instructions)]
    (* horiz depth)))

(comment
  (answer-1 example-input)

  (->> (slurp "day2input.txt")
       (format "[%s]")
       edn/read-string
       (partition 2)
       answer-1))

(defn answer-2 [instructions]
  (let [[horiz depth _]
        (reduce (fn [[horiz depth aim] [direction amount]]
                  (case direction
                    forward [(+ horiz amount) (+ depth (* aim amount)) aim]
                    down    [horiz depth (+ aim amount)]
                    up      [horiz depth (- aim amount)]))
                [0 0 0]
                instructions)]
    (* horiz depth)))

(comment
  (answer-2 example-input)

  (->> (slurp "day2input.txt")
       (format "[%s]")
       edn/read-string
       (partition 2)
       answer-2))
