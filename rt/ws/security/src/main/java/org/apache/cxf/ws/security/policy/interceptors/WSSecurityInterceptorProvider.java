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
name|ws
operator|.
name|security
operator|.
name|policy
operator|.
name|interceptors
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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|ws
operator|.
name|policy
operator|.
name|AbstractPolicyInterceptorProvider
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
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|PolicyBasedWSS4JInInterceptor
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
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|PolicyBasedWSS4JOutInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|SP12Constants
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|WSSecurityInterceptorProvider
extends|extends
name|AbstractPolicyInterceptorProvider
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|6222118542914666817L
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Collection
argument_list|<
name|QName
argument_list|>
name|ASSERTION_TYPES
decl_stmt|;
static|static
block|{
name|ASSERTION_TYPES
operator|=
operator|new
name|ArrayList
argument_list|<
name|QName
argument_list|>
argument_list|()
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|TRANSPORT_BINDING
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|ASYMMETRIC_BINDING
argument_list|)
expr_stmt|;
name|ASSERTION_TYPES
operator|.
name|add
argument_list|(
name|SP12Constants
operator|.
name|SYMMETRIC_BINDING
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WSSecurityInterceptorProvider
parameter_list|()
block|{
name|super
argument_list|(
name|ASSERTION_TYPES
argument_list|)
expr_stmt|;
name|this
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|PolicyBasedWSS4JOutInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|this
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|PolicyBasedWSS4JOutInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|this
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|PolicyBasedWSS4JInInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|this
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|PolicyBasedWSS4JInInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

