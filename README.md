# gRPC-first-project
Клиент отправляет запрос серверу ("start")
Сервер отвечает только на запрос "start"
По запросу клиента серверная часть делает снимки с веб-камеры, сжимает и сохраняет на своей стороне. Затем отправляет поочередно полученные снимки, предварительно преобразовав их в массив байтов, и числовые данные