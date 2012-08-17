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
name|jca
operator|.
name|cxf
operator|.
name|test
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
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
name|BusException
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
name|i18n
operator|.
name|Message
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
name|feature
operator|.
name|Feature
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
name|interceptor
operator|.
name|AbstractBasicInterceptorProvider
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
name|service
operator|.
name|model
operator|.
name|AbstractPropertiesHolder
import|;
end_import

begin_class
specifier|public
class|class
name|DummyBus
extends|extends
name|AbstractBasicInterceptorProvider
implements|implements
name|Bus
block|{
comment|// for initialise behaviours
specifier|static
name|int
name|initializeCount
decl_stmt|;
specifier|static
name|int
name|shutdownCount
decl_stmt|;
specifier|static
name|boolean
name|correctThreadContextClassLoader
decl_stmt|;
specifier|static
name|boolean
name|throwException
decl_stmt|;
specifier|static
name|Bus
name|bus
init|=
operator|new
name|DummyBus
argument_list|()
decl_stmt|;
specifier|static
name|String
index|[]
name|invokeArgs
decl_stmt|;
specifier|static
name|String
name|cxfHome
init|=
literal|"File:/local/temp"
decl_stmt|;
specifier|public
specifier|static
name|void
name|reset
parameter_list|()
block|{
name|initializeCount
operator|=
literal|0
expr_stmt|;
name|shutdownCount
operator|=
literal|0
expr_stmt|;
name|correctThreadContextClassLoader
operator|=
literal|false
expr_stmt|;
name|throwException
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasExtensionByName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|Bus
name|init
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|BusException
block|{
name|initializeCount
operator|++
expr_stmt|;
name|correctThreadContextClassLoader
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|==
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jca
operator|.
name|cxf
operator|.
name|ManagedConnectionFactoryImpl
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
expr_stmt|;
if|if
condition|(
name|throwException
condition|)
block|{
throw|throw
operator|new
name|BusException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"tested bus exception!"
argument_list|,
operator|(
name|ResourceBundle
operator|)
literal|null
argument_list|,
operator|new
name|Object
index|[]
block|{}
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|bus
return|;
block|}
specifier|public
name|void
name|shutdown
parameter_list|(
name|boolean
name|wait
parameter_list|)
block|{
name|shutdownCount
operator|++
expr_stmt|;
block|}
comment|//    @Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getExtension
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|extensionType
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
comment|//    @Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|setExtension
parameter_list|(
name|T
name|extension
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|extensionType
parameter_list|)
block|{      }
comment|//    @Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getConfiguration
parameter_list|(
name|AbstractPropertiesHolder
name|props
parameter_list|,
name|T
name|defaultValue
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
comment|//    @Override
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
specifier|static
name|boolean
name|isCorrectThreadContextClassLoader
parameter_list|()
block|{
return|return
name|correctThreadContextClassLoader
return|;
block|}
specifier|public
specifier|static
name|void
name|setCorrectThreadContextClassLoader
parameter_list|(
name|boolean
name|correct
parameter_list|)
block|{
name|DummyBus
operator|.
name|correctThreadContextClassLoader
operator|=
name|correct
expr_stmt|;
block|}
specifier|public
specifier|static
name|int
name|getInitializeCount
parameter_list|()
block|{
return|return
name|initializeCount
return|;
block|}
specifier|public
specifier|static
name|void
name|setInitializeCount
parameter_list|(
name|int
name|count
parameter_list|)
block|{
name|DummyBus
operator|.
name|initializeCount
operator|=
name|count
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|s
parameter_list|,
name|Object
name|o
parameter_list|)
block|{     }
specifier|public
name|Collection
argument_list|<
name|Feature
argument_list|>
name|getFeatures
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

