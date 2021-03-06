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
name|systest
operator|.
name|jaxrs
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
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
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
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|ClassHelper
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
name|JAXRSInvoker
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
name|impl
operator|.
name|SecurityContextImpl
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
name|OperationResourceInfo
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
name|MessageContentsList
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
name|systest
operator|.
name|jaxrs
operator|.
name|security
operator|.
name|SecureBookStore
import|;
end_import

begin_class
specifier|public
class|class
name|CustomJAXRSInvoker
extends|extends
name|JAXRSInvoker
block|{
annotation|@
name|Override
specifier|public
name|Object
name|invoke
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|requestParams
parameter_list|,
name|Object
name|resourceObject
parameter_list|)
block|{
name|OperationResourceInfo
name|ori
init|=
name|exchange
operator|.
name|get
argument_list|(
name|OperationResourceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|Method
name|m
init|=
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|realClass
init|=
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|exchange
operator|.
name|getBus
argument_list|()
argument_list|,
name|resourceObject
argument_list|)
decl_stmt|;
name|Principal
name|p
init|=
operator|new
name|SecurityContextImpl
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
operator|.
name|getUserPrincipal
argument_list|()
decl_stmt|;
if|if
condition|(
name|realClass
operator|==
name|SecureBookStore
operator|.
name|class
operator|&&
literal|"getThatBook"
operator|.
name|equals
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
literal|"baddy"
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|MessageContentsList
argument_list|(
name|Response
operator|.
name|status
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|FORBIDDEN
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|invoke
argument_list|(
name|exchange
argument_list|,
name|requestParams
argument_list|,
name|resourceObject
argument_list|)
return|;
block|}
block|}
end_class

end_unit

