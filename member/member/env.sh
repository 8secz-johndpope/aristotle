docker run -v ~/work/data/mysql/ss_db_central:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=ss_db_central --net=host -d mysql:5.5
