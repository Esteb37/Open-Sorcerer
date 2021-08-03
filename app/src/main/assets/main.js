Parse.Cloud.define('sendPush', function(request, response) {
  const query = new Parse.Query(Parse.Installation);
  query.equalTo('device_id', request.params.deviceId);
  Parse.Push.send({
    where: query,
    data: {
      alert: request.params.message,
      title: request.params.title
    }
  },
  { useMasterKey: true }
  )
  .then(function() {
    response.success();
  }, function(error) {
    response.error(error);
  });
});