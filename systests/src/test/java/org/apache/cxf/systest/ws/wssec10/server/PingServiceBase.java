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
name|ws
operator|.
name|wssec10
operator|.
name|server
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceContext
import|;
end_import

begin_import
import|import
name|wssec
operator|.
name|wssec10
operator|.
name|IPingService
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|PingServiceBase
implements|implements
name|IPingService
block|{
annotation|@
name|Resource
specifier|protected
name|WebServiceContext
name|ctx
decl_stmt|;
specifier|protected
name|PingServiceBase
parameter_list|()
block|{     }
specifier|public
name|java
operator|.
name|lang
operator|.
name|String
name|echo
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|request
parameter_list|)
block|{
comment|//System.out.println("echo(" + request + ")");
return|return
name|request
return|;
block|}
block|}
end_class

end_unit

