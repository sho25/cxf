begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|common
operator|.
name|util
operator|.
name|ProxyHelper
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
name|model
operator|.
name|UserResource
import|;
end_import

begin_comment
comment|/**  * Factory for creating proxy clients.  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|JAXRSClientFactory
block|{
specifier|private
name|JAXRSClientFactory
parameter_list|()
block|{               }
comment|/**      * Creates a proxy      * @param baseAddress baseAddress      * @param cls resource class, if not interface then a CGLIB proxy will be created      * @return typed proxy      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|create
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|)
argument_list|,
name|cls
argument_list|)
return|;
block|}
comment|/**      * Creates a proxy      * @param baseURI baseURI      * @param cls resource class, if not interface then a CGLIB proxy will be created      * @return typed proxy      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|create
parameter_list|(
name|URI
name|baseURI
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|baseURI
argument_list|,
name|cls
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Creates a proxy      * @param baseURI baseURI      * @param cls resource class, if not interface then a CGLIB proxy will be created      * @param inheritHeaders if true then existing proxy headers will be inherited by       *        subresource proxies if any      * @return typed proxy      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|create
parameter_list|(
name|URI
name|baseURI
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|boolean
name|inheritHeaders
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
name|getBean
argument_list|(
name|baseURI
operator|.
name|toString
argument_list|()
argument_list|,
name|cls
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setInheritHeaders
argument_list|(
name|inheritHeaders
argument_list|)
expr_stmt|;
return|return
name|bean
operator|.
name|create
argument_list|(
name|cls
argument_list|)
return|;
block|}
comment|/**      * Creates a proxy      * @param baseAddress baseAddress      * @param cls resource class, if not interface then a CGLIB proxy will be created      * @param config classpath location of Spring configuration resource      * @return typed proxy      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|create
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|String
name|configLocation
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
name|getBean
argument_list|(
name|baseAddress
argument_list|,
name|cls
argument_list|,
name|configLocation
argument_list|)
decl_stmt|;
return|return
name|bean
operator|.
name|create
argument_list|(
name|cls
argument_list|)
return|;
block|}
comment|/**      * Creates a proxy      * @param baseAddress baseAddress      * @param cls resource class, if not interface then a CGLIB proxy will be created      *        This class is expected to have a root JAXRS Path annotation containing      *        template variables, for ex, "/path/{id1}/{id2}"        * @param config classpath location of Spring configuration resource      * @param varValues values to replace root Path template variables         * @return typed proxy      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|create
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|String
name|configLocation
parameter_list|,
name|Object
modifier|...
name|varValues
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
name|getBean
argument_list|(
name|baseAddress
argument_list|,
name|cls
argument_list|,
name|configLocation
argument_list|)
decl_stmt|;
return|return
name|bean
operator|.
name|create
argument_list|(
name|cls
argument_list|,
name|varValues
argument_list|)
return|;
block|}
comment|/**      * Creates a proxy      * @param baseAddress baseAddress      * @param cls proxy class, if not interface then a CGLIB proxy will be created      * @param providers list of providers      * @return typed proxy      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|create
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|providers
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|baseAddress
argument_list|,
name|cls
argument_list|,
name|providers
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Creates a proxy      * @param baseAddress baseAddress      * @param cls proxy class, if not interface then a CGLIB proxy will be created      * @param providers list of providers      * @param config classpath location of Spring configuration resource      * @return typed proxy      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|create
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|providers
parameter_list|,
name|String
name|configLocation
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
name|getBean
argument_list|(
name|baseAddress
argument_list|,
name|cls
argument_list|,
name|configLocation
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
return|return
name|bean
operator|.
name|create
argument_list|(
name|cls
argument_list|)
return|;
block|}
comment|/**      * Creates a proxy which will do basic authentication      * @param baseAddress baseAddress      * @param cls proxy class, if not interface then a CGLIB proxy will be created      * @param username username      * @param password password      * @param config classpath location of Spring configuration resource      * @return typed proxy      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|create
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|String
name|configLocation
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
name|getBean
argument_list|(
name|baseAddress
argument_list|,
name|cls
argument_list|,
name|configLocation
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
return|return
name|bean
operator|.
name|create
argument_list|(
name|cls
argument_list|)
return|;
block|}
comment|/**      * Creates a proxy using user resource model      * @param baseAddress baseAddress      * @param cls proxy class, if not interface then a CGLIB proxy will be created      * @param modelRef model location      * @return typed proxy      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|createFromModel
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|String
name|modelRef
parameter_list|,
name|String
name|configLocation
parameter_list|)
block|{
return|return
name|createFromModel
argument_list|(
name|baseAddress
argument_list|,
name|cls
argument_list|,
name|modelRef
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|,
name|configLocation
argument_list|)
return|;
block|}
comment|/**      * Creates a proxy using user resource model      * @param baseAddress baseAddress      * @param cls proxy class, if not interface then a CGLIB proxy will be created      * @param modelRef model location      * @param providers list of providers      * @return typed proxy      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|createFromModel
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|String
name|modelRef
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|providers
parameter_list|,
name|String
name|configLocation
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
name|WebClient
operator|.
name|getBean
argument_list|(
name|baseAddress
argument_list|,
name|configLocation
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setModelRef
argument_list|(
name|modelRef
argument_list|)
expr_stmt|;
return|return
name|bean
operator|.
name|create
argument_list|(
name|cls
argument_list|)
return|;
block|}
comment|/**      * Creates a proxy using user resource model      * @param baseAddress baseAddress      * @param cls proxy class, if not interface then a CGLIB proxy will be created      * @param modelBeans model beans      * @return typed proxy      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|createFromModel
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|List
argument_list|<
name|UserResource
argument_list|>
name|modelBeans
parameter_list|,
name|String
name|configLocation
parameter_list|)
block|{
return|return
name|createFromModel
argument_list|(
name|baseAddress
argument_list|,
name|cls
argument_list|,
name|modelBeans
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|,
name|configLocation
argument_list|)
return|;
block|}
comment|/**      * Creates a proxy using user resource model      * @param baseAddress baseAddress      * @param cls proxy class, if not interface then a CGLIB proxy will be created      * @param modelBeans model beans      * @param providers list of providers      * @return typed proxy      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|createFromModel
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|List
argument_list|<
name|UserResource
argument_list|>
name|modelBeans
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|providers
parameter_list|,
name|String
name|configLocation
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
name|WebClient
operator|.
name|getBean
argument_list|(
name|baseAddress
argument_list|,
name|configLocation
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setModelBeans
argument_list|(
name|modelBeans
argument_list|)
expr_stmt|;
return|return
name|bean
operator|.
name|create
argument_list|(
name|cls
argument_list|)
return|;
block|}
comment|/**      * Creates a proxy, baseURI will be set to Client currentURI      *         * @param client Client instance      * @param cls proxy class, if not interface then a CGLIB proxy will be created      * @return typed proxy      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|fromClient
parameter_list|(
name|Client
name|client
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|fromClient
argument_list|(
name|client
argument_list|,
name|cls
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Creates a proxy, baseURI will be set to Client currentURI      * @param client Client instance      * @param cls proxy class, if not interface then a CGLIB proxy will be created      * @param inheritHeaders if true then existing Client headers will be inherited by new proxy       *        and subresource proxies if any       * @return typed proxy      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|fromClient
parameter_list|(
name|Client
name|client
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|boolean
name|inheritHeaders
parameter_list|)
block|{
name|T
name|proxy
init|=
name|create
argument_list|(
name|client
operator|.
name|getCurrentURI
argument_list|()
argument_list|,
name|cls
argument_list|,
name|inheritHeaders
argument_list|)
decl_stmt|;
if|if
condition|(
name|inheritHeaders
condition|)
block|{
name|WebClient
operator|.
name|client
argument_list|(
name|proxy
argument_list|)
operator|.
name|headers
argument_list|(
name|client
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|WebClient
operator|.
name|copyProperties
argument_list|(
name|WebClient
operator|.
name|client
argument_list|(
name|proxy
argument_list|)
argument_list|,
name|client
argument_list|)
expr_stmt|;
return|return
name|proxy
return|;
block|}
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|create
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|InvocationHandler
name|handler
parameter_list|)
block|{
return|return
name|cls
operator|.
name|cast
argument_list|(
name|ProxyHelper
operator|.
name|getProxy
argument_list|(
name|cls
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|cls
block|,
name|Client
operator|.
name|class
block|}
argument_list|,
name|handler
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|JAXRSClientFactoryBean
name|getBean
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|String
name|configLocation
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
name|WebClient
operator|.
name|getBean
argument_list|(
name|baseAddress
argument_list|,
name|configLocation
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|cls
argument_list|)
expr_stmt|;
return|return
name|bean
return|;
block|}
block|}
end_class

end_unit

