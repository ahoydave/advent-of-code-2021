(ns day6)

(def breed-age 6)
(def init-age 8)

(def example-input [3 4 3 1 2])

(defn step [pop]
  (let [num-new-fish (count (filter #(= 0 %) pop))
        aged-fish (map (fn [x]
                         (if (= x 0)
                           breed-age
                           (dec x)))
                       pop)]
    (concat aged-fish (repeat num-new-fish init-age))))

(defn save-step-n-times [n pop]
  (loop [fish-population pop
         step-count 0
         acc []]
    (if (= step-count n)
      (conj acc [step-count fish-population])
      (recur (step fish-population)
             (inc step-count)
             (conj acc [step-count fish-population])))))

(defn pop-after-steps [n init-pop]
  (loop [pop init-pop
         counter n]
    (if (= 0 counter)
      pop
      (recur (step pop) (dec counter)))))

(comment
  (concat '(1 2 3) (vec '(2 3)))
  (repeat 3 5)
  example-input
  (step example-input)
  (step (step example-input))
  (save-step-n-times 5 example-input)
  (pop-after-steps 5 example-input)
  (count (pop-after-steps 80 example-input)))

(defn input-to-indexed [pop]
  (reduce (fn [acc n]
            (update acc n inc))
          (vec (repeat (inc init-age) 0))
          pop))

(defn step-indexed [pop-index]
  (-> (rest pop-index)
      vec
      (update breed-age + (first pop-index))
      (conj (first pop-index))))

(defn count-indexed-pop [pop-index]
  (reduce +
          0
          pop-index))

(defn pop-index-after-steps [n init-pop]
  (loop [pop (input-to-indexed init-pop)
         counter n]
    (if (= 0 counter)
      (count-indexed-pop pop)
      (recur (step-indexed pop) (dec counter)))))


(comment
  (input-to-indexed example-input)
  (input-to-indexed (step (step example-input)))
  (step-indexed (step-indexed (input-to-indexed example-input)))
  (count-indexed-pop (input-to-indexed example-input))
  (pop-index-after-steps 80 example-input)
  (count (pop-after-steps 80 example-input)))

(comment
  (loop [acc []
         index (vec (concat [1] (repeat init-age 0)))
         counter 80]
    (if (= 0 counter)
      acc
      (recur (conj acc index)
             (step-indexed index)
             (dec counter)))))