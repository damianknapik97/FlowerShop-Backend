Set-Variable -Name "containerName" -Value "postgreSQL"
docker pull postgres;
docker stop $(docker ps -a -q -f "name=$(Get-Variable -Name "containerName" -ValueOnly)");
docker rm $(docker ps -a -q -f "name=$(Get-Variable -Name "containerName" -ValueOnly)");
#WARNING ! Deletes all unused volumes !!!!!!!!!
docker volume rm $(docker volume ls -qf dangling=true)
#WARNING ! Deletes all unused volumes !!!!!!!!!
docker run --name "$(Get-Variable -Name "containerName" -ValueOnly)" -e POSTGRES_PASSWORD=admin -e POSTGRES_USER=admin -p 5430:5432 -d postgres;
