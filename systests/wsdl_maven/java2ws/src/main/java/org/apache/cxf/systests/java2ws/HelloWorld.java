begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systests
operator|.
name|java2ws
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_interface
annotation|@
name|WebService
specifier|public
interface|interface
name|HelloWorld
block|{
name|String
name|sayHi
parameter_list|(
name|String
name|text
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

