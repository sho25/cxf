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
name|tracing
operator|.
name|brave
package|;
end_package

begin_import
import|import
name|brave
operator|.
name|http
operator|.
name|HttpAdapter
import|;
end_import

begin_import
import|import
name|brave
operator|.
name|http
operator|.
name|HttpServerParser
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
name|StringUtils
import|;
end_import

begin_class
specifier|public
class|class
name|HttpServerSpanParser
extends|extends
name|HttpServerParser
block|{
annotation|@
name|Override
specifier|protected
parameter_list|<
name|Req
parameter_list|>
name|String
name|spanName
parameter_list|(
name|HttpAdapter
argument_list|<
name|Req
argument_list|,
name|?
argument_list|>
name|adapter
parameter_list|,
name|Req
name|request
parameter_list|)
block|{
return|return
name|buildSpanDescription
argument_list|(
name|adapter
operator|.
name|path
argument_list|(
name|request
argument_list|)
argument_list|,
name|adapter
operator|.
name|method
argument_list|(
name|request
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|String
name|buildSpanDescription
parameter_list|(
specifier|final
name|String
name|path
parameter_list|,
specifier|final
name|String
name|method
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|method
argument_list|)
condition|)
block|{
return|return
name|path
return|;
block|}
return|return
name|method
operator|+
literal|" "
operator|+
name|path
return|;
block|}
block|}
end_class

end_unit

