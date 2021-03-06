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
name|service
operator|.
name|invoker
package|;
end_package

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
name|common
operator|.
name|i18n
operator|.
name|BundleUtils
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
name|interceptor
operator|.
name|Fault
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
name|message
operator|.
name|Exchange
import|;
end_import

begin_comment
comment|/**  * This invoker implementation calls a Factory to create the service object.  *  */
end_comment

begin_class
specifier|public
class|class
name|FactoryInvoker
extends|extends
name|AbstractInvoker
block|{
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|FactoryInvoker
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|Factory
name|factory
decl_stmt|;
comment|/**      * Create a FactoryInvoker object.      *      * @param factory the factory used to create service object.      */
specifier|public
name|FactoryInvoker
parameter_list|(
name|Factory
name|factory
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
block|}
specifier|public
name|FactoryInvoker
parameter_list|()
block|{     }
specifier|public
name|void
name|setFactory
parameter_list|(
name|Factory
name|f
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|f
expr_stmt|;
block|}
specifier|public
name|Object
name|getServiceObject
parameter_list|(
name|Exchange
name|ex
parameter_list|)
block|{
try|try
block|{
return|return
name|factory
operator|.
name|create
argument_list|(
name|ex
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Fault
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"CREATE_SERVICE_OBJECT_EXC"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|releaseServiceObject
parameter_list|(
specifier|final
name|Exchange
name|ex
parameter_list|,
name|Object
name|obj
parameter_list|)
block|{
name|factory
operator|.
name|release
argument_list|(
name|ex
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSingletonFactory
parameter_list|()
block|{
return|return
name|factory
operator|instanceof
name|SingletonFactory
return|;
block|}
block|}
end_class

end_unit

