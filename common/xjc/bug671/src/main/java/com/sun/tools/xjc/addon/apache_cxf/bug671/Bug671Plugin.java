begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|com
operator|.
name|sun
operator|.
name|tools
operator|.
name|xjc
operator|.
name|addon
operator|.
name|apache_cxf
operator|.
name|bug671
package|;
end_package

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|ErrorHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|tools
operator|.
name|xjc
operator|.
name|BadCommandLineException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|tools
operator|.
name|xjc
operator|.
name|Options
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|tools
operator|.
name|xjc
operator|.
name|Plugin
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|tools
operator|.
name|xjc
operator|.
name|outline
operator|.
name|Outline
import|;
end_import

begin_comment
comment|/**  * Thin wrapper around the Bug671Plugin. This must be in the com.sun.tools.xjc.addon package  * for it to work with Java 6. See https://issues.apache.org/jira/browse/CXF-1880.  */
end_comment

begin_class
specifier|public
class|class
name|Bug671Plugin
extends|extends
name|Plugin
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xjc
operator|.
name|bug671
operator|.
name|Bug671Plugin
name|impl
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xjc
operator|.
name|bug671
operator|.
name|Bug671Plugin
argument_list|(
name|this
argument_list|)
decl_stmt|;
comment|/* (non-Javadoc)      * @see com.sun.tools.xjc.Plugin#getOptionName()      */
annotation|@
name|Override
specifier|public
name|String
name|getOptionName
parameter_list|()
block|{
return|return
name|impl
operator|.
name|getOptionName
argument_list|()
return|;
block|}
comment|/* (non-Javadoc)      * @see com.sun.tools.xjc.Plugin#getUsage()      */
annotation|@
name|Override
specifier|public
name|String
name|getUsage
parameter_list|()
block|{
return|return
name|impl
operator|.
name|getUsage
argument_list|()
return|;
block|}
specifier|public
name|void
name|onActivated
parameter_list|(
name|Options
name|opts
parameter_list|)
throws|throws
name|BadCommandLineException
block|{
name|impl
operator|.
name|onActivated
argument_list|(
name|opts
argument_list|)
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see com.sun.tools.xjc.Plugin#run(com.sun.tools.xjc.outline.Outline,      *   com.sun.tools.xjc.Options, org.xml.sax.ErrorHandler)      */
annotation|@
name|Override
specifier|public
name|boolean
name|run
parameter_list|(
name|Outline
name|outline
parameter_list|,
name|Options
name|opt
parameter_list|,
name|ErrorHandler
name|errorHandler
parameter_list|)
throws|throws
name|SAXException
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

