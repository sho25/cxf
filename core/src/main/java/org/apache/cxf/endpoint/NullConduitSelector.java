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
name|endpoint
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
name|transport
operator|.
name|Conduit
import|;
end_import

begin_comment
comment|/**  * Strategy for null Conduit retrieval.   * An instance of this class is set on the Exchange to clear  * the current ConduitSelector, as a work-around for broken   * Exchange.remove(ConduitSelector.class) semantics.  */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|NullConduitSelector
implements|implements
name|ConduitSelector
block|{
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
comment|/**      * Called prior to the interceptor chain being traversed.      *       * @param message the current Message      */
specifier|public
name|void
name|prepare
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
comment|// nothing to do
block|}
comment|/**      * Called when a Conduit is actually required.      *       * @param message      * @return the Conduit to use for mediation of the message      */
specifier|public
name|Conduit
name|selectConduit
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Called on completion of the MEP for which the Conduit was required.      *       * @param exchange represents the completed MEP      */
specifier|public
name|void
name|complete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// nothing to do
block|}
comment|/**      * @return the encapsulated Endpoint      */
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
comment|/**      * @param ep the endpoint to encapsulate      */
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|Endpoint
name|ep
parameter_list|)
block|{
name|endpoint
operator|=
name|ep
expr_stmt|;
block|}
block|}
end_class

end_unit

