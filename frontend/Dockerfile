FROM node

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY package.json /usr/src/app/

RUN ["npm", "install"]
RUN ["npm", "install", "angular"]
RUN ["npm", "install", "ng-toast", "--production"]
RUN cp -r /usr/src/app/node_modules/ng-toast/dist/ /usr/src/app/toast/

COPY . /usr/src/app/

EXPOSE 8080
CMD [ "node", "server.js" ]
