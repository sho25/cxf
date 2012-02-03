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
name|clustering
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
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
import|;
end_import

begin_comment
comment|/**  * This feature may be applied to a Client so as to enable  * load distribution amongst a set of target endpoints or addresses  * Note that this feature changes the conduit on the fly and thus makes  * the Client not thread safe.  */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|LoadDistributorFeature
extends|extends
name|FailoverFeature
block|{
annotation|@
name|Override
specifier|protected
name|FailoverTargetSelector
name|getTargetSelector
parameter_list|()
block|{
return|return
operator|new
name|LoadDistributorTargetSelector
argument_list|()
return|;
block|}
block|}
end_class

end_unit

