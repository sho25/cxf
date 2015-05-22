begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|sample
operator|.
name|rs
operator|.
name|service
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|JAXRSServerFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|servlet
operator|.
name|CXFServlet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|SpringApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|EnableAutoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|builder
operator|.
name|SpringApplicationBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|embedded
operator|.
name|EmbeddedServletContainerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|embedded
operator|.
name|ServletRegistrationBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|web
operator|.
name|SpringBootServletInitializer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|ImportResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|lifecycle
operator|.
name|SingletonResourceProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bus
operator|.
name|spring
operator|.
name|SpringBus
import|;
end_import

begin_import
import|import
name|sample
operator|.
name|rs
operator|.
name|service
operator|.
name|HelloService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|JAXRSBindingFactory
import|;
end_import

begin_class
annotation|@
name|Configuration
annotation|@
name|EnableAutoConfiguration
annotation|@
name|ImportResource
argument_list|(
block|{
literal|"classpath:META-INF/cxf/cxf.xml"
block|}
argument_list|)
specifier|public
class|class
name|SampleRestWSApplication
extends|extends
name|SpringBootServletInitializer
block|{
annotation|@
name|Autowired
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|SpringApplication
operator|.
name|run
argument_list|(
name|SampleRestWSApplication
operator|.
name|class
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Bean
specifier|public
name|ServletRegistrationBean
name|servletRegistrationBean
parameter_list|(
name|ApplicationContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|ServletRegistrationBean
argument_list|(
operator|new
name|CXFServlet
argument_list|()
argument_list|,
literal|"/services/*"
argument_list|)
return|;
block|}
annotation|@
name|Bean
specifier|public
name|Server
name|rsServer
parameter_list|()
block|{
name|Bus
name|bus
init|=
operator|(
name|Bus
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
name|Bus
operator|.
name|DEFAULT_BUS_ID
argument_list|)
decl_stmt|;
name|JAXRSServerFactoryBean
name|endpoint
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setServiceBean
argument_list|(
operator|new
name|HelloService
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setAddress
argument_list|(
literal|"/helloservice"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
return|return
name|endpoint
operator|.
name|create
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|SpringApplicationBuilder
name|configure
parameter_list|(
name|SpringApplicationBuilder
name|application
parameter_list|)
block|{
return|return
name|application
operator|.
name|sources
argument_list|(
name|SampleRestWSApplication
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

