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
name|javascript
operator|.
name|fortest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

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

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|MTOM
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
name|helpers
operator|.
name|IOUtils
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|feature
operator|.
name|Features
argument_list|(
name|features
operator|=
literal|"org.apache.cxf.feature.LoggingFeature"
argument_list|)
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"uri:org.apache.cxf.javascript.fortest"
argument_list|)
annotation|@
name|MTOM
argument_list|(
name|enabled
operator|=
literal|true
argument_list|,
name|threshold
operator|=
literal|0
argument_list|)
specifier|public
class|class
name|MtoMImpl
implements|implements
name|MtoM
block|{
specifier|private
name|String
name|returnData
decl_stmt|;
specifier|private
name|MtoMParameterBeanNoDataHandler
name|lastBean
decl_stmt|;
specifier|private
name|MtoMParameterBeanWithDataHandler
name|lastDHBean
decl_stmt|;
specifier|public
name|MtoMImpl
parameter_list|()
block|{
name|InputStream
name|someData
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"org/apache/cxf/javascript/cxf-utils.js"
argument_list|)
decl_stmt|;
try|try
init|(
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
init|)
block|{
name|InputStreamReader
name|isr
init|=
operator|new
name|InputStreamReader
argument_list|(
name|someData
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|isr
argument_list|,
name|sw
argument_list|,
literal|4096
argument_list|)
expr_stmt|;
name|returnData
operator|=
name|sw
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|lastBean
operator|=
literal|null
expr_stmt|;
name|lastDHBean
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|MtoMParameterBeanNoDataHandler
name|getLastBean
parameter_list|()
block|{
return|return
name|lastBean
return|;
block|}
specifier|public
name|void
name|receiveNonXmlDH
parameter_list|(
name|MtoMParameterBeanNoDataHandler
name|param
parameter_list|)
block|{
name|lastBean
operator|=
name|param
expr_stmt|;
block|}
specifier|public
name|MtoMParameterBeanWithDataHandler
name|getLastDHBean
parameter_list|()
block|{
return|return
name|lastDHBean
return|;
block|}
specifier|public
name|void
name|receiveNonXmlDH
parameter_list|(
name|MtoMParameterBeanWithDataHandler
name|param
parameter_list|)
block|{
name|lastDHBean
operator|=
name|param
expr_stmt|;
block|}
specifier|public
name|void
name|receiveNonXmlNoDH
parameter_list|(
name|MtoMParameterBeanNoDataHandler
name|param
parameter_list|)
block|{
name|lastBean
operator|=
name|param
expr_stmt|;
block|}
specifier|public
name|MtoMParameterBeanWithDataHandler
name|sendNonXmlDH
parameter_list|()
block|{
name|MtoMParameterBeanWithDataHandler
name|result
init|=
operator|new
name|MtoMParameterBeanWithDataHandler
argument_list|()
decl_stmt|;
name|result
operator|.
name|setOrdinary
argument_list|(
literal|"ordinarius"
argument_list|)
expr_stmt|;
name|result
operator|.
name|setNotXml10
argument_list|(
operator|new
name|DataHandler
argument_list|(
name|returnData
argument_list|,
literal|"text/plain;charset=utf-8"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

