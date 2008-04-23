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
name|aegis
operator|.
name|mtom
operator|.
name|fortest
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
annotation|@
name|WebService
annotation|@
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|MTOM
specifier|public
class|class
name|MtomTestImpl
implements|implements
name|MtomTest
block|{
specifier|public
specifier|static
specifier|final
name|String
name|STRING_DATA
init|=
literal|"What rough beast, its hour come at last,"
operator|+
literal|" slouches toward Bethlehem to be born?"
decl_stmt|;
specifier|private
name|DataHandlerBean
name|lastDhBean
decl_stmt|;
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|acceptDataHandler
parameter_list|(
name|DataHandlerBean
name|dhBean
parameter_list|)
block|{
name|lastDhBean
operator|=
name|dhBean
expr_stmt|;
block|}
specifier|public
name|DataHandlerBean
name|getLastDhBean
parameter_list|()
block|{
return|return
name|lastDhBean
return|;
block|}
specifier|public
name|DataHandlerBean
name|produceDataHandlerBean
parameter_list|()
block|{
name|DataHandlerBean
name|dhBean
init|=
operator|new
name|DataHandlerBean
argument_list|()
decl_stmt|;
name|dhBean
operator|.
name|setName
argument_list|(
literal|"legion"
argument_list|)
expr_stmt|;
comment|// since we know that the code has no lower threshold for using attachments,
comment|// we can just return a fairly short string.
name|dhBean
operator|.
name|setDataHandler
argument_list|(
operator|new
name|DataHandler
argument_list|(
name|STRING_DATA
argument_list|,
literal|"text/plain;charset=utf-8"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|dhBean
return|;
block|}
block|}
end_class

end_unit

