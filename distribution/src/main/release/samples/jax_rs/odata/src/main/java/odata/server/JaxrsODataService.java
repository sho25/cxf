begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|odata
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|edmx
operator|.
name|EdmxReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|OData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|ODataHttpHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|ServiceMetadata
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/DemoService.svc"
argument_list|)
specifier|public
class|class
name|JaxrsODataService
block|{
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"{id:.*}"
argument_list|)
specifier|public
name|Response
name|service
parameter_list|(
annotation|@
name|Context
name|HttpServletRequest
name|req
parameter_list|,
annotation|@
name|Context
name|HttpServletResponse
name|resp
parameter_list|)
block|{
name|String
name|requestMapping
init|=
name|req
operator|.
name|getContextPath
argument_list|()
operator|+
name|req
operator|.
name|getServletPath
argument_list|()
operator|+
literal|"/DemoService.svc"
decl_stmt|;
name|req
operator|.
name|setAttribute
argument_list|(
literal|"requestMapping"
argument_list|,
name|requestMapping
argument_list|)
expr_stmt|;
comment|// create odata handler and configure it with EdmProvider and Processor
name|OData
name|odata
init|=
name|OData
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|ServiceMetadata
name|edm
init|=
name|odata
operator|.
name|createServiceMetadata
argument_list|(
operator|new
name|DemoEdmProvider
argument_list|()
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|EdmxReference
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|ODataHttpHandler
name|handler
init|=
name|odata
operator|.
name|createHandler
argument_list|(
name|edm
argument_list|)
decl_stmt|;
name|handler
operator|.
name|register
argument_list|(
operator|new
name|DemoEntityCollectionProcessor
argument_list|()
argument_list|)
expr_stmt|;
comment|// let the handler do the work
name|handler
operator|.
name|process
argument_list|(
name|req
argument_list|,
name|resp
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit
