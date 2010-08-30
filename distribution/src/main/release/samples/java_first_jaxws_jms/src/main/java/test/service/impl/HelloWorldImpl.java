begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|test
operator|.
name|service
operator|.
name|impl
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

begin_import
import|import
name|test
operator|.
name|service
operator|.
name|HelloWorld
import|;
end_import

begin_class
annotation|@
name|WebService
specifier|public
class|class
name|HelloWorldImpl
implements|implements
name|HelloWorld
block|{
comment|/* (non-Javadoc)  * @see test.IHello#sayHi(java.lang.String)  */
specifier|public
name|String
name|sayHi
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|name
return|;
block|}
block|}
end_class

end_unit

