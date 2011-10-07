begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|wsn
operator|.
name|client
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|wsaddressing
operator|.
name|W3CEndpointReference
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
name|wsn
operator|.
name|util
operator|.
name|WSNHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|b_2
operator|.
name|CreatePullPointResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|bw_2
operator|.
name|UnableToCreatePullPointFault
import|;
end_import

begin_class
specifier|public
class|class
name|CreatePullPoint
implements|implements
name|Referencable
block|{
specifier|private
specifier|final
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|bw_2
operator|.
name|CreatePullPoint
name|createPullPoint
decl_stmt|;
specifier|private
specifier|final
name|W3CEndpointReference
name|epr
decl_stmt|;
specifier|public
name|CreatePullPoint
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|this
argument_list|(
name|WSNHelper
operator|.
name|createWSA
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CreatePullPoint
parameter_list|(
name|W3CEndpointReference
name|epr
parameter_list|)
block|{
name|this
operator|.
name|createPullPoint
operator|=
name|WSNHelper
operator|.
name|getPort
argument_list|(
name|epr
argument_list|,
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|bw_2
operator|.
name|CreatePullPoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|epr
operator|=
name|epr
expr_stmt|;
block|}
specifier|public
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|bw_2
operator|.
name|CreatePullPoint
name|getCreatePullPoint
parameter_list|()
block|{
return|return
name|createPullPoint
return|;
block|}
specifier|public
name|W3CEndpointReference
name|getEpr
parameter_list|()
block|{
return|return
name|epr
return|;
block|}
specifier|public
name|PullPoint
name|create
parameter_list|()
throws|throws
name|UnableToCreatePullPointFault
block|{
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|b_2
operator|.
name|CreatePullPoint
name|request
init|=
operator|new
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|b_2
operator|.
name|CreatePullPoint
argument_list|()
decl_stmt|;
name|CreatePullPointResponse
name|response
init|=
name|createPullPoint
operator|.
name|createPullPoint
argument_list|(
name|request
argument_list|)
decl_stmt|;
return|return
operator|new
name|PullPoint
argument_list|(
name|response
operator|.
name|getPullPoint
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

