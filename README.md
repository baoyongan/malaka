# malaka
this project base on malaka-thrift,zookeeper or esb(another project)
for example:
# Client request:
          public class Test
          public void main(String[] args){
          Map<String, String> in = new HashMap<String, String>();
          in.put("id", "template");
          in.put("page", "index");
          ......................
          Response              r=com.zcbl.malaka.rpc.common.Malaka.remote("weixin.template.message.send").times(1).server("192.168.1.170:4000").request(in)
              .result();
          System.out.println(r.getResponse());
          }
          }
# Service Registry：
          @Malaka("weixin")
          public class LogRpc
          {
            static PersistManager log = new PersistManager();

            @Url(".template.message.send")
            public void log(Request request, Response response)
            {
              Map<String, String> message = request.getRequest();
              Remote r = new Remote();
              r.toBean(message);
              log.persit(r);
              response.getResponse().put("rescode", "200");
              response.getResponse().put("resdes", "success");
            }
          }
