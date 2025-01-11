# QueueSystemModeling
Построение модели системы массового обслуживания 

## Постановка задачи

Целью работы является построение модели системы массового обслуживания. Модель должна описывать компоненты ВС (вычислительной системы) на таком уровне детализации, который будет имитировать ее функциональность и структуру.
Модель дает приближенное описание объекта исследования с целью получения требуемых результатов с определенной точностью и достоверностью.
В данной работе будет использоваться иммитационная модель. В результате работы с помощью анализа модели будет найдена оптимальная конфигурация исследуемой ВС.

Расшифровка варианта

ИБ	ИЗ1	ПЗ2	Д10З1	Д10О3	Д2П1	Д2Б3	ОР1	ОД3

Источники: 
ИБ – бесконечный источник; 
И31 – пуассоновский закон распределения заявок; 
 
Приборы: 
П32 – равномерный закон распределения времени обслуживания; 
 
 Описание дисциплин постановки и выбора: 
 
  Дисциплина буферизации: 
  Д1031 – заполнение буферной памяти по кольцу; 

  Дисциплина отказа: 
  Д1003 – самая старая в буфере; 

Дисциплина постановки на обслуживание:
Д2П1 – выбор прибора: приоритет по номеру прибора;  

   Д2Б3 – выбор заявки из буфера по кольцу; 

Виды отображения результатов работы программной модели:

Динамическое отражение результатов:
 ОД3 – временные диаграммы, текущее состояние;

Отражение результатов после сбора статистики: 
ОР1 – сводная таблица результатов.

## Формализованная схема и описание СМО

 
- Здесь Иi (i= 1..n) – источник заявок, который генерирует заявки, а все вместе n источников создают входной поток заявок в систему.
Каждая заявка приходит в СМО со своими характеристиками. Это Tвх — время генерации заявки (время поступления её в СМО) и
номер заявки составленный из номера источника, сгенерировавшего заявку, и порядкового номера заявки от этого источника. Например, (2.3) – третья заявка от второго источника.
- П — приборы, которые обслуживают заявки и создают выходной поток заявок после обслуживания.
- БП — буферная память (место для хранения очереди заявок). В общей памяти хранятся заявки от различных источников. Порядок их записи в БП определяется только дисциплиной буферизации.
- ДП — диспетчер постановки заявок.
- ДВ — диспетчер выбора заявок.

