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
name|transfer
operator|.
name|shared
operator|.
name|faults
package|;
end_package

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
name|transfer
operator|.
name|shared
operator|.
name|TransferConstants
import|;
end_import

begin_comment
comment|/**  * This fault MUST be generated when a request specifies a resource  * that is not known.  */
end_comment

begin_class
specifier|public
class|class
name|UnknownResource
extends|extends
name|WSTransferFault
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2925090710469446447L
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SUBCODE
init|=
literal|"UnknownResource"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REASON
init|=
literal|"The resource is not known."
decl_stmt|;
specifier|public
name|UnknownResource
parameter_list|()
block|{
name|super
argument_list|(
name|REASON
argument_list|,
literal|null
argument_list|,
operator|new
name|QName
argument_list|(
name|TransferConstants
operator|.
name|TRANSFER_2011_03_NAMESPACE
argument_list|,
name|SUBCODE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
