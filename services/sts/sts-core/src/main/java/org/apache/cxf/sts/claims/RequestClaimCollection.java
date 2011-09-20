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
name|sts
operator|.
name|claims
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_comment
comment|/**  * This holds a collection of RequestClaims.  */
end_comment

begin_class
specifier|public
class|class
name|RequestClaimCollection
extends|extends
name|java
operator|.
name|util
operator|.
name|ArrayList
argument_list|<
name|RequestClaim
argument_list|>
block|{
specifier|private
name|URI
name|dialect
decl_stmt|;
specifier|public
name|URI
name|getDialect
parameter_list|()
block|{
return|return
name|dialect
return|;
block|}
specifier|public
name|void
name|setDialect
parameter_list|(
name|URI
name|dialect
parameter_list|)
block|{
name|this
operator|.
name|dialect
operator|=
name|dialect
expr_stmt|;
block|}
block|}
end_class

end_unit

