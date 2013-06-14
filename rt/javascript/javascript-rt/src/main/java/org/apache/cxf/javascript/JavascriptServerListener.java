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
name|javascript
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
name|binding
operator|.
name|soap
operator|.
name|SoapBinding
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
name|endpoint
operator|.
name|ServerLifeCycleListener
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|JavascriptServerListener
implements|implements
name|ServerLifeCycleListener
block|{
specifier|public
name|JavascriptServerListener
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{     }
specifier|public
name|void
name|startServer
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
if|if
condition|(
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|instanceof
name|SoapBinding
condition|)
block|{
comment|//found a SOAP binding, add the javascript generation interceptor
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|JavascriptGetInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|stopServer
parameter_list|(
name|Server
name|server
parameter_list|)
block|{     }
block|}
end_class

end_unit

